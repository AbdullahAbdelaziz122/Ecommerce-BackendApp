package com.BMS.exceptions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ResourceAlreadyExist extends RuntimeException {
    private String message;

}
