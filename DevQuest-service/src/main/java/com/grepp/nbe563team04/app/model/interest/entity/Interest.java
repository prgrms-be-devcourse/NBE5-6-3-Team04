package com.grepp.nbe563team04.app.model.interest.entity;

import com.grepp.nbe563team04.app.model.interest.code.Type;
import com.grepp.nbe563team04.app.model.member.entity.MemberInterest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestId;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String interestName;

    @OneToMany(mappedBy = "interest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberInterest> memberInterests;

    public String roadmapUrl;
}