//package br.com.bebblue.ws;
//
//import br.com.xptosystems.security.ws.NotifyResponse;
//import br.com.xptosystems.dao.Dao;
//import br.com.xptosystems.security.UserToken;
//import br.com.xptosystems.security.Users;
//import br.com.xptosystems.securitys.dao.UserTokenDao;
//import br.com.xptosystems.securitys.dao.UsersDao;
//import com.google.gson.Gson;
//import java.math.BigInteger;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.UUID;
//import javax.ws.rs.FormParam;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import org.apache.commons.codec.digest.DigestUtils;
//
//@Path("/auth")
//public class AuthWS {
//@POST
//    @Path("/in")
//    @Produces({MediaType.APPLICATION_JSON})
//    public synchronized Response auth(@FormParam("login") String login, @FormParam("password") String password) {
//    // public synchronized Response auth() {
//        NotifyResponse notifyResponse = new NotifyResponse();
//        Gson gson = new Gson();
//        try {
//            notifyResponse.setStatus(0);
//            login = login.trim();
//            if (login.trim().isEmpty()) {
//                notifyResponse.setObject("LOGIN INVÁLIDO!");
//                return Response.status(200).entity(gson.toJson(notifyResponse)).build();
//            }
//            if (password.isEmpty()) {
//                notifyResponse.setObject("INVALID PASSWORD!");
//                return Response.status(200).entity(gson.toJson(notifyResponse)).build();
//            }
//            UsersDao userDao = new UsersDao();
//            Users u = new Users();
//////            MessageDigest m = MessageDigest.getInstance("MD5");
////            m.update(password.getBytes(), 0, password.length());
////            password = new BigInteger(1, m.digest()).toString(16);
//            u.setEmail(login);
//            u.setPassword(password);
//            u = userDao.authUser(u);
//            String jsonResult = null;
//            if (u == null) {
//                notifyResponse.setObject("INVALID USER!");
//                return Response.status(200).entity(gson.toJson(notifyResponse)).build();
//            }
//            if (!u.getActive()) {
//                password = "";
//                notifyResponse.setObject("INATIVE ACCOUNT!");
//                return Response.status(200).entity(gson.toJson(notifyResponse)).build();
//            }
//            UserToken userToken = null;
//            try {
//
//                userToken = tokenGenerate(u);
//                Thread.sleep(1000);
//                // new Dao().save(userToken, true);
//                try {
//                    // jsonResult = gson.toJson(userToken);
//                } catch (Exception e) {
//                    e.getMessage();
//                }
//            } catch (Exception e) {
//                e.getStackTrace();
//            }
//            return Response.status(200).entity(gson.toJson(userToken)).build();
//
//        } catch (Exception e) {
//            e.getMessage();
//        }
//        notifyResponse.setObject("ERRO!");
//        return Response.status(200).entity(gson.toJson(notifyResponse)).build();
//    }
//
//    protected UserToken tokenGenerate(Users u) throws NoSuchAlgorithmException {
//        new UserTokenDao().update(u.getId());
//        UUID uuidX = UUID.randomUUID();
//        MessageDigest access_token = MessageDigest.getInstance("MD5");
//        access_token.update(uuidX.toString().replace("-", "_").toLowerCase().getBytes(), 0, uuidX.toString().replace("-", "_").toLowerCase().length());
//        String token_md5 = new BigInteger(1, access_token.digest()).toString(16);
//        DigestUtils.md5Hex(token_md5);
//        Dao dao = new Dao();
//        UserToken ut = new UserToken();
//        ut.setUsers((Users) dao.find(u));
//        ut.setAccessToken(token_md5);
//        dao.save(ut, true);
//        return ut;
//    }
//}