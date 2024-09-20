package com.kh.soundcast.member.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSocial {
   private int memberSocialMemberNo;
   private String memberSocialSocialId;
   private String memberSocialSocialName;
}
