package com.example.xiaoredshu.auth.domain.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/7/7 16:45
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDO {

    private Long id;

    private String username;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
