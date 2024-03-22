package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataBaseSetting {

    private String url = "jdbc:oracle:thin:@dbexp19.cn.sub:1573:dbexp19";
    private String username = "CBWMIG";
    private String password = "TtgQ9xE4V_QqPQ5N";
}
