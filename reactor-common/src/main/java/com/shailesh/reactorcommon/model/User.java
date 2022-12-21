package com.shailesh.reactorcommon.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User dto
 */
@Data
@Builder
public class User {
    private int id;
    private String name;
    private String role;
}
