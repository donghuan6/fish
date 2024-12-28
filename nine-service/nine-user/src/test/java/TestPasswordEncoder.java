import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPasswordEncoder {

    public static void main(String[] args) {
        BCryptPasswordEncoder ps=new BCryptPasswordEncoder();

        String admin = ps.encode("123");
        System.out.println(admin);

        System.out.println(ps.matches("123", admin));

    }

}
