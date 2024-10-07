package com.blueOcean.humanResourceSystem.DTO;

import lombok.Data;

import java.util.Set;

@Data
public class HeaderManuResponse {
    private Set<String> manus;
    private Set<String> dropdown;

}
