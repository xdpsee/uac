package com.zhenhui.demo.uac.core.dataobject;

import com.zhenhui.demo.uac.common.SocialType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class SocialAccount extends Entity {

    @NonNull
    private SocialType type;
    @NonNull
    private Long openId;
    @NonNull
    private String nickname;
    @NonNull
    private String avatar;

    private Long userId = 0L;
}
