package com.sparta.schedulemanager_extension.repository;

import com.sparta.schedulemanager_extension.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
