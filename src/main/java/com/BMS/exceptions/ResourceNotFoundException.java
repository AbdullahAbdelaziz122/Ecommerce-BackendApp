package com.BMS.exceptions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException{
    private String message;


}
