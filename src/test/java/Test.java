import com.dunpju.gen.Gen;
import com.yumi.jerrcode.ErrCode;

public class Test {
    public static void main(String[] args) {
        Gen.code("D:\\php\\jgen\\src\\test\\java\\ymal",
                "com.yumi.jerrcode",
                "D:\\php\\jgen\\src\\test\\java\\com\\yumi\\jerrcode\\ErrCode.java",
                "com.dunpju.errcode.Code");
        System.out.println("ddd");
        System.out.println(ErrCode.SUCCESS.getCode());
        System.out.println(ErrCode.SUCCESS.getMessage());
    }
}
