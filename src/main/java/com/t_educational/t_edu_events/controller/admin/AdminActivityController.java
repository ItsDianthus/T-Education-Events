package com.t_educational.t_edu_events.controller.admin;

import com.t_educational.t_edu_events.model.Activity;
import com.t_educational.t_edu_events.repository.ActivityRepository;
import com.t_educational.t_edu_events.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/events/{eventID}/activities")
@RequiredArgsConstructor
public class AdminActivityController {

    private final ActivityRepository activityRepository;
    private final EventRepository eventRepository;

    // GET /admin/events/{eventID}/activities — получение списка активностей мероприятия.
    @GetMapping
    public ResponseEntity<List<Activity>> getActivities(@PathVariable("eventID") UUID eventID) {
        if (!eventRepository.existsById(eventID)) {
            return ResponseEntity.notFound().build();
        }
        List<Activity> activities = activityRepository.findByEventId(eventID);
        return ResponseEntity.ok(activities);
    }

    // POST /admin/events/{eventID}/activities — создание новой активности для мероприятия.
    @PostMapping
    public ResponseEntity<Activity> createActivity(@PathVariable("eventID") UUID eventID,
                                                   @RequestBody Activity activity) {
        return eventRepository.findById(eventID)
                .map(event -> {
                    // Связываем активность с мероприятием
                    activity.setEvent(event);
                    Activity savedActivity = activityRepository.save(activity);
                    return ResponseEntity.ok(savedActivity);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /admin/events/{eventID}/activities/{activityID} — обновление активности
    @PutMapping("/{activityID}")
    public ResponseEntity<Activity> updateActivity(@PathVariable("eventID") UUID eventID,
                                                   @PathVariable("activityID") UUID activityID,
                                                   @RequestBody Activity updatedActivity) {
        return (ResponseEntity<Activity>) eventRepository.findById(eventID)
                .flatMap(event -> activityRepository.findById(activityID)
                        .map(existingActivity -> {
                            // Проверяем, что активность действительно относится к данному мероприятию
                            if (!existingActivity.getEvent().getId().equals(event.getId())) {
                                return ResponseEntity.<Activity>status(HttpStatus.BAD_REQUEST).build();
                            }
                            existingActivity.setName(updatedActivity.getName());
                            existingActivity.setDescription(updatedActivity.getDescription());
                            existingActivity.setPlace(updatedActivity.getPlace());
                            existingActivity.setStartTime(updatedActivity.getStartTime());
                            existingActivity.setEndTime(updatedActivity.getEndTime());
                            Activity saved = activityRepository.save(existingActivity);
                            return ResponseEntity.ok(saved);
                        }))
                .orElseGet(() -> ResponseEntity.<Activity>status(HttpStatus.BAD_REQUEST).build());
    }


    // DELETE /admin/events/{eventID}/activities/{activityID} — удаление активности.
    @DeleteMapping("/{activityID}")
    public ResponseEntity<Object> deleteActivity(@PathVariable("eventID") UUID eventID,
                                               @PathVariable("activityID") UUID activityID) {
        return eventRepository.findById(eventID)
                .flatMap(event -> activityRepository.findById(activityID)
                        .map(activity -> {
                            // Проверяем, что активность принадлежит этому мероприятию
                            if (!activity.getEvent().getId().equals(event.getId())) {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                            }
                            activityRepository.delete(activity);
                            return ResponseEntity.noContent().build();
                        }))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
