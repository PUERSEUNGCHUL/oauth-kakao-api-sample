package kr.co.puerpuella.oathserver;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.util.Base64;

public class Test {
    public static void main(String[] args) {
        String a = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImY1ZjRiZjQ2ZTUyYjMxZDliNjI0OWY3MzA5YWQwMzM4NDAwNjgwY2QiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI2OTI4MTUyMTI2NDgtdG0zM3VvZ2ZrbGV2NXV1a3I2ZGtxMmdiN3Q0NGQ4NXMuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI2OTI4MTUyMTI2NDgtdG0zM3VvZ2ZrbGV2NXV1a3I2ZGtxMmdiN3Q0NGQ4NXMuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDI5OTg4NzI2MTU4MDgyNzk3MjkiLCJlbWFpbCI6ImxoaGwxMDI5QGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdF9oYXNoIjoiSUU3aEgzNGJpM0ptUi1nUFJ0OWd0ZyIsIm5hbWUiOiLsnbTsirnssqAiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUNnOG9jTFhTVjJuYmJ4V2l4bjJVeHNuaFU5T1liSjl6QklrNkVUc1Q5cFZlaTFqUlE9czk2LWMiLCJnaXZlbl9uYW1lIjoi7Iq57LKgIiwiZmFtaWx5X25hbWUiOiLsnbQiLCJsb2NhbGUiOiJrbyIsImlhdCI6MTY5ODk4MTk3OCwiZXhwIjoxNjk4OTg1NTc4fQ.gFAK7GekGlQY_dZ0QPUGHDdxuHoa8qyujRe2yWdYuICtCVwuYqKWsVqtokHbPdrx8sF8o_Pu7Hne-WxYL27NUFuktR1WCwtWpqyR2s8yTar_qu-VG1YvinqwjdT6Np45IBvm55VJxpPem2grntRCWe6VQYHPqP4wKlJpspWZ4cwGFUYUzVySdJD8Gjp1YXov4l58G2leBt-O8-bjg91G6knsrpRlNYeXBZSOhdXC9nlZEd3G6ArfXMSvN0Oa_BPn5pXm-3cn0G-DcV8nQ2UWyCZwSal3ORPOo1h5wZVjjRrO7xiUjEzgZfradPma00urs3fKaKDi2jeAzGPbSMLkhA";

        Jwts.parser()
                .parseClaimsJws(a)
                .getBody();
        Jws<Claims> b = Jwts.parserBuilder()
                .build()
                .parseClaimsJws(a);

        new

        System.out.println("decoder.decode(a) = " + b.getBody().get("email"));
    }
}
