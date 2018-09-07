package hello.service;

import hello.entity.Answer;
import hello.entity.Question;
import hello.repository.AnswerRepository;
import hello.repository.QusetionRepository;
import hello.repository.ThemeRepository;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionsService {

    private final ThemeRepository themeRepository;

    private final QusetionRepository qusetionRepository;

    private final AnswerRepository answerRepository;

    @Autowired
    public QuestionsService(ThemeRepository themeRepository, QusetionRepository qusetionRepository, AnswerRepository answerRepository) {
        this.themeRepository = themeRepository;
        this.qusetionRepository = qusetionRepository;
        this.answerRepository = answerRepository;
    }

    @Transactional
    public JSONObject getQuestionsByThemeId(Long themeId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("theme",getThemeByThemeId(themeId));
        List<Question> questionList = qusetionRepository.getQuestionsByThemeId(themeId);
        JSONArray jsonArray = new JSONArray();
        for (Question question : questionList) {
            Map<String, Object> map = new HashMap<>();
            List<Answer> answerList = answerRepository.getAnswersByQuestionId(question.getQuestionId());
            map.put("question", question.getQuestion());
            map.put("answer", answerList.stream().map(Answer::getAnswer).toArray());
            jsonArray.add(JSONObject.fromObject(map));
        }
        jsonObject.put("questions",jsonArray);
        return jsonObject;
    }

    private String getThemeByThemeId(Long themeId){
        return themeRepository.getThemeByThemeId(themeId).getTheme();
    }

}
