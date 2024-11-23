package org.livestream.api.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.livestream.api.error.ApiErrorEnum;
import org.livestream.api.service.ILivingRoomService;

import org.livestream.api.vo.LivingRoomInitVO;
import org.livestream.api.vo.req.LivingRoomReqVO;
import org.livestream.api.vo.req.OnlinePkReqVO;
import org.livestream.api.vo.resp.LivingRoomPageRespVO;
import org.livestream.api.vo.resp.LivingRoomRespVO;
import org.livestream.common.interfaces.dto.PageWrapper;
import org.livestream.common.interfaces.utils.ConvertBeanUtils;
import org.livestream.im.constants.AppIdEnum;
import org.livestream.living.interfaces.dto.LivingPkRespDTO;
import org.livestream.living.interfaces.dto.LivingRoomReqDTO;
import org.livestream.living.interfaces.dto.LivingRoomRespDTO;
import org.livestream.living.interfaces.rpc.ILivingRoomRpc;
import org.livestream.user.dto.UserDTO;
import org.livestream.user.interfaces.IUserRpc;
import org.livestream.web.starter.context.QiyuRequestContext;
import org.livestream.web.starter.error.ErrorAssert;
import org.livestream.web.starter.error.QiyuErrorException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author idea
 * @Date: Created in 21:15 2023/7/19
 * @Description
 */
@Service
public class LivingRoomServiceImpl implements ILivingRoomService {

    @DubboReference
    private IUserRpc userRpc;
    @DubboReference
    private ILivingRoomRpc livingRoomRpc;

    @Override
    public LivingRoomPageRespVO list(LivingRoomReqVO livingRoomReqVO) {
        PageWrapper<LivingRoomRespDTO>  resultPage = livingRoomRpc.list(ConvertBeanUtils.convert(livingRoomReqVO,LivingRoomReqDTO.class));
        LivingRoomPageRespVO livingRoomPageRespVO = new LivingRoomPageRespVO();
        livingRoomPageRespVO.setList(ConvertBeanUtils.convertList(resultPage.getList(), LivingRoomRespVO.class));
        livingRoomPageRespVO.setHasNext(resultPage.isHasNext());
        return livingRoomPageRespVO;
    }

    @Override
    public Integer startingLiving(Integer type) {
        Long userId = QiyuRequestContext.getUserId();
        UserDTO userDTO = userRpc.getByUserId(userId);
        LivingRoomReqDTO livingRoomReqDTO = new LivingRoomReqDTO();
        livingRoomReqDTO.setAnchorId(userId);
        livingRoomReqDTO.setRoomName("主播-" + QiyuRequestContext.getUserId() + "的直播间");
        livingRoomReqDTO.setCovertImg(userDTO.getAvatar());
        livingRoomReqDTO.setType(type);
        return livingRoomRpc.startLivingRoom(livingRoomReqDTO);
    }

    @Override
    public boolean onlinePk(OnlinePkReqVO onlinePkReqVO) {
        LivingRoomReqDTO reqDTO = ConvertBeanUtils.convert(onlinePkReqVO,LivingRoomReqDTO.class);
        reqDTO.setAppId(AppIdEnum.QIYU_LIVE_BIZ.getCode());
        reqDTO.setPkObjId(QiyuRequestContext.getUserId());
        LivingPkRespDTO tryOnlineStatus = livingRoomRpc.onlinePk(reqDTO);
        ErrorAssert.isTure(tryOnlineStatus.isOnlineStatus(), new QiyuErrorException(-1,tryOnlineStatus.getMsg()));
        return true;
    }

    @Override
    public boolean closeLiving(Integer roomId) {
        LivingRoomReqDTO livingRoomReqDTO = new LivingRoomReqDTO();
        livingRoomReqDTO.setRoomId(roomId);
        livingRoomReqDTO.setAnchorId(QiyuRequestContext.getUserId());
        return livingRoomRpc.closeLiving(livingRoomReqDTO);
    }

    @Override
    public LivingRoomInitVO anchorConfig(Long userId, Integer roomId) {
        LivingRoomRespDTO respDTO = livingRoomRpc.queryByRoomId(roomId);
        ErrorAssert.isNotNull(respDTO,ApiErrorEnum.LIVING_ROOM_END);
        Map<Long,UserDTO> userDTOMap = userRpc.batchQueryUserInfo(Arrays.asList(respDTO.getAnchorId(),userId).stream().distinct().collect(Collectors.toList()));
        UserDTO anchor = userDTOMap.get(respDTO.getAnchorId());
        UserDTO watcher = userDTOMap.get(userId);
        LivingRoomInitVO respVO = new LivingRoomInitVO();
        respVO.setAnchorNickName(anchor.getNickName());
        respVO.setWatcherNickName(watcher.getNickName());
        respVO.setUserId(userId);
        //给定一个默认的头像
        respVO.setAvatar(StringUtils.isEmpty(anchor.getAvatar())?"https://s1.ax1x.com/2022/12/18/zb6q6f.png":anchor.getAvatar());
        respVO.setWatcherAvatar(watcher.getAvatar());
        if (respDTO == null || respDTO.getAnchorId() == null || userId == null) {
            //这种就是属于直播间已经不存在的情况了
            respVO.setRoomId(-1);
        } else {
            respVO.setRoomId(respDTO.getId());
            respVO.setAnchorId(respDTO.getAnchorId());
            respVO.setAnchor(respDTO.getAnchorId().equals(userId));
        }
        respVO.setDefaultBgImg("https://picst.sunbangyan.cn/2023/08/29/waxzj0.png");
        return respVO;
    }

}
