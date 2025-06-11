package com.grepp.nbe562team04.model.interest.dto;

import com.grepp.nbe562team04.model.interest.code.Type;
import com.grepp.nbe562team04.model.interest.entity.Interest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InterestDto {

    private Long interestId;
    private Type type;
    private String interestName;
    private String roadmapUrl;

    public InterestDto (Interest interest){
        this.interestId = interest.getInterestId();
        this.type = interest.getType();
        this.interestName = interest.getInterestName();
        this.roadmapUrl = interest.getRoadmapUrl();
    }
}
