package com.example.xiaoredshu.auth.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/7/7 12:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private String nickName;

    private LocalDateTime createTime;
}
