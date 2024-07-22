package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.request.ChangeActivationStateRequest;
import com.example.waguwagu.domain.request.RiderUpdateRequest;


public interface RiderService {
    void saveRider(Rider rider); // auth domain에서 받은 rider 목록 저장
    void updateRider(Long id, RiderUpdateRequest req); // auth domain에서 받은 rider 내용 업데이트
    Rider getById(Long id); // id 별로 rider 가져오기
    void deleteById(Long id); // id로 찾아서 삭제
    void changeActivationState(Long id, ChangeActivationStateRequest req);
}
