package hello.service;

import hello.WxInterface.GetUserInfo;
import hello.entity.User;
import hello.repository.UserRepository;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final GetUserInfo getUserInfo;

    private final UserRepository userRepository;

    @Autowired
    public UserService(GetUserInfo getUserInfo, UserRepository userRepository) {
        this.getUserInfo = getUserInfo;
        this.userRepository = userRepository;
    }

    @Transactional
    public JSONObject addNewUser(String encryptedData, String iv, String code){
        JSONObject json = getUserInfo.decodeUserInfo(encryptedData,iv,code);
        User user = new User(json.getJSONObject("userInfo"));
        String openId = user.getOpenId();
        if(null == userRepository.isUserExist(openId)){
            userRepository.save(user);
        }
        return json;
    }

    private String getAvatarUrl(String openId){
        return userRepository.getAvatarUrl(openId);
    }

    private String getNickName(String openId){
        return userRepository.getNickName(openId);
    }

    public User getReceiverInfo(String openId) {
        if(userRepository.getUserByOpenId(openId).isPresent()) {
            return userRepository.getUserByOpenId(openId).get();
        }
        return null;
    }

    @Transactional
    public JSONArray getBunchReceiverInfo(String openId) {
        JSONArray json = JSONArray.fromObject(openId);
        JSONArray result = new JSONArray();
        json.forEach(id ->
                result.add(getReceiverInfo(id.toString()))
        );
        return result;
    }

    @Transactional
    public void addNewUser( String nickName,String openId,Long times, String avatarUrl,Double money) {
        if(null == userRepository.isUserExist(openId)){
            userRepository.save(new User(nickName,openId, times, avatarUrl,money));
        }
    }

    @Transactional
    public void updateTimes(String openId) {
        userRepository.updateMoney(openId);
        userRepository.updateTimes(openId);
    }
}
