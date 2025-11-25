package com.vitalioleksenko.csp.util;

import com.vitalioleksenko.csp.models.Task;
import com.vitalioleksenko.csp.models.enums.TaskStatus;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import static com.vitalioleksenko.csp.models.enums.TaskStatus.IN_PROGRESS;
import static com.vitalioleksenko.csp.models.enums.TaskStatus.IN_REVIEW;

public class TaskSpecification {

    public static Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, cb) -> {
            if (status == null) return cb.conjunction();
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Task> inGroup(Integer groupId) {
        return (root, query, cb) -> {
            if (groupId == null) return cb.conjunction();
            return cb.equal(root.get("group").get("groupId"), groupId);
        };
    }

    public static Specification<Task> visibleForUser(Integer userId) {
        return (root, query, cb) -> {
            if (userId == null) return cb.conjunction();

            Join<Object, Object> group = root.join("group");
            Join<Object, Object> members = group.join("members");

            query.distinct(true);
            return cb.equal(members.get("user").get("userId"), userId);
        };
    }
    public static Specification<Task> isActive() {
        return (root, query, cb) -> root.get("status").in(IN_PROGRESS, IN_REVIEW);
    }
}


