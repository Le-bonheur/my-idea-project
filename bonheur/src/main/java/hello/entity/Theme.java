package hello.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Theme {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long themeId;

    private String theme;

    public Theme() {
    }

    public Theme(String theme) {
        this.theme = theme;
    }

    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
