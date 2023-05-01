package com.example.ShopeeClone.config;

import com.example.ShopeeClone.entity.User;
import com.example.ShopeeClone.repositories.UserRepositories;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "7234743777217A25432A462D4A614E645267556B58703273357638782F413F44";
    @Autowired
    private UserRepositories userRepositories;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public User authenticateUser(HttpServletRequest request) throws AuthenticationException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            throw new AuthenticationException("sai token");
        }
        jwt = authHeader.substring(7);
        userEmail = extractUsername(jwt);
        if (userEmail != null) {
            Optional<User> existedUser = userRepositories.findByEmail(userEmail);
            User user = existedUser.orElseThrow(() -> new AuthenticationException("User not found"));
            if (!isTokenValid(jwt, user)) {
                throw new AuthenticationException("sai token");
            }
            return user;
        }
        throw new AuthenticationException("sai token");
    }
//    Phương thức extractClaim() trong mã nguồn của Spring Security JWT dùng để lấy ra một claim (thuộc tính)
//    trong payload của JWT bằng cách truyền vào token (chuỗi JWT) và một claimsResolver (hàm xử lý claims)
//    nhận vào đối tượng Claims và trả về kiểu dữ liệu T.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }
    // khởi  tạo một JWTtoken

    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date((System.currentTimeMillis())))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean isTokenValid(String token,UserDetails userDetails){
        final  String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    //    Phương thức extractAllClaims() trong Spring Security được sử dụng để giải mã một chuỗi JWT
//    và trả về tất cả các thông tin của payload trong chuỗi JWT đó dưới dạng một đối tượng Claims.
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


/*phương thức dùng để lấy ra một secret key từ một chuỗi String có độ dài là 256-bit
 để sử dụng cho việc mã hóa và giải mã JWT. Key được trả về dưới dạng đối tượng Key
của thư viện javax.crypto để sử dụng trong các phương thức liên quan đến mã hóa.*/
private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
