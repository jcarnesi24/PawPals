package pawpals_db.Chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import pawpals_db.Users.BasicUser;

import javax.persistence.*;

import pawpals_db.Users.BasicUser;

@Entity
@Table(name = "conversations")
@Data
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String otherUsername;

    @Column
    private int otherId;

    @Column
    private String otherUserType;

    @ManyToOne
    @JoinColumn(name = "basicUser_id")
    @JsonIgnore
    BasicUser thisUser;

    public Conversation() {}

    public Conversation(String name, int id, String userType) {
        this.otherUsername = name;
        this.otherId = id;
        this.otherUserType = userType;
    }

    public Conversation(String name, int id, String userType, BasicUser thisUser) {
        this.otherUsername = name;
        this.otherId = id;
        this.otherUserType = userType;
        this.thisUser = thisUser;
    }

    public String getName() { return otherUsername; }

    public void setName(String name) { this.otherUsername = name; }

    public int getId() { return otherId; }

    public void setId(int id) { this.otherId = id; }

    public String getOtherUserType() { return otherUserType; }

    public void setOtherUserType(String userType) { this.otherUserType = userType; }
    }

