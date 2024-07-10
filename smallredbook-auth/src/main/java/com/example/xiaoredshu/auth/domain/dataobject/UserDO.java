package com.example.xiaoredshu.auth.domain.dataobject;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDO {
    private Long id;

    @NotBlank(message = "昵称不能为空")
    private String username;

    private LocalDateTime createTime;

    private LocalDateTime  updateTime;


}