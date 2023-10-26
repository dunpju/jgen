package io.dunpju.gen;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TypeConvert {
    private String source;
    private String target;
    private String pkg;
}
