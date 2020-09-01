package com.wxtemplate.api.entity.VO;

import com.wxtemplate.api.entity.Car;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CarVo extends Car {
    private static final long serialVersionUID = 1L;
    private String imageurls;
}
