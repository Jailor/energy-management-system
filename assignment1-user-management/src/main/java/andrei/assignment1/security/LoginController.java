package andrei.assignment1.security;

import andrei.assignment1.dtos.security.LoginDTORequest;
import andrei.assignment1.dtos.security.LoginDTOResponse;
import andrei.assignment1.security.JwtService;
import andrei.assignment1.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/user-api/login")
public class LoginController {
    @Autowired
    LoginService loginService;
    @Autowired
    JwtService jwtTokenUtil;

    @PostMapping()
    public @ResponseBody LoginDTOResponse login(@RequestBody LoginDTORequest loginDTORequest) {
        LoginDTOResponse response = loginService.login(loginDTORequest.getUsername(), loginDTORequest.getPassword());
        if(response.isValid()){
            response.setJsonWebToken(jwtTokenUtil.generateToken(loginDTORequest.getUsername()));
        }
        return response;
    }

    @GetMapping()
    public String loginTest() {
        return "test";
    }
}
