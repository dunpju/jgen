import com.dunpju.gen.Gen;
import com.yumi.enums.EnumYesOrNo;
import com.yumi.jerrcode.ErrCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        /*Gen.code("D:\\php\\jgen\\src\\test\\java\\ymal",
                "com.yumi.jerrcode",
                "D:\\php\\jgen\\src\\test\\java\\com\\yumi\\jerrcode\\ErrCode.java",
                "com.dunpju.errcode.Code");
        System.out.println("ddd");
        System.out.println(ErrCode.SUCCESS.getCode());
        System.out.println(ErrCode.SUCCESS.getMessage());*/
        Pattern patten = Pattern.compile("(\\s+)");
        String str = "name=yes_or_no   flag=是否:no-1-否,yes-2-是";
        System.out.println(str);
        Matcher matcher = patten.matcher(str);
        if (matcher.find()) {
            System.out.println(matcher.group());
            str = matcher.replaceAll(" ");
        }
        System.out.println(str);

        Gen.enums("com.yumi.enums",
                "name=yes_or_no   flag=是否:no-1-否,yes-2-是",
                "D:\\php\\jgen\\src\\test\\java\\com\\yumi\\enums");
        System.out.println(EnumYesOrNo.NO.getCode());

    }
}
