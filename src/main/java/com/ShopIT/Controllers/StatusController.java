package com.ShopIT.Controllers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
public class StatusController {
    String Content = "<div align=\"center\">This is the backend server of SHOPIT<BR><BR><BR><img src=\"https://elasticbeanstalk-ap-south-1-665793442236.s3.ap-south-1.amazonaws.com/resources/images/0085dd88-a820-409d-a52c-263fda6a2a5d.jpg\" alt=\"APPLICATION LOGO\" style=\"border: 2px solid black; border-radius: 100px; height:200px;\"><BR><BR><BR><h1>Designed and Developed by <b>Pranav Bisaria</b></h1><br><br><br><a href = \"http://shopit-env-1.eba-kjhuufsq.ap-south-1.elasticbeanstalk.com/swagger-ui/index.html\">API Documentation</a></div>";
    @Operation(summary = "This is developer information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Server Working Successfully", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/")
    public ResponseEntity<?> test(){
        return new ResponseEntity<>(Content, OK);
    }
}
