package com.ShopIT.Repository;

import com.ShopIT.Models.QuestionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionsRepo extends JpaRepository<QuestionModel, Integer> {
}
