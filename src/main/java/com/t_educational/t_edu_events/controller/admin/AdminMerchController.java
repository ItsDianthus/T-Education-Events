package com.t_educational.t_edu_events.controller.admin;

import com.t_educational.t_edu_events.model.EventCategoryMerch;
import com.t_educational.t_edu_events.repository.EventCategoryMerchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/events/{eventId}/categories/{categoryId}/merch")
@RequiredArgsConstructor
public class AdminMerchController {

    private final EventCategoryMerchRepository eventCategoryMerchRepository;

    @GetMapping
    public ResponseEntity<List<EventCategoryMerch>> getMerchByCategory(
            @PathVariable UUID eventId,
            @PathVariable UUID categoryId) {
        List<EventCategoryMerch> merchList = eventCategoryMerchRepository.findByEventIdAndCategoryId(eventId, categoryId);
        return ResponseEntity.ok(merchList);
    }

    // POST /admin/events/{eventId}/categories/{categoryId}/merch - создание новой позиции мерча
    @PostMapping
    public ResponseEntity<EventCategoryMerch> createMerch(@PathVariable UUID eventId,
                                                          @PathVariable UUID categoryId,
                                                          @RequestBody EventCategoryMerch merchData) {
        merchData.setEventId(eventId);
        merchData.setCategoryId(categoryId);
        EventCategoryMerch savedMerch = eventCategoryMerchRepository.save(merchData);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMerch);
    }

    // PUT /admin/events/{eventId}/categories/{categoryId}/merch/{merchId} - обновление информации о позиции мерча
    @PutMapping("/{merchId}")
    public ResponseEntity<EventCategoryMerch> updateMerch(@PathVariable UUID eventId,
                                                          @PathVariable UUID categoryId,
                                                          @PathVariable UUID merchId,
                                                          @RequestBody EventCategoryMerch merchData) {
        EventCategoryMerch existing = eventCategoryMerchRepository.findById(merchId)
                .orElseThrow(() -> new RuntimeException("Merch not found"));
        existing.setItemName(merchData.getItemName());
        existing.setRequiredPoints(merchData.getRequiredPoints());
        existing.setAvailable(merchData.isAvailable());
        EventCategoryMerch updated = eventCategoryMerchRepository.save(existing);
        return ResponseEntity.ok(updated);
    }

    // DELETE /admin/events/{eventId}/categories/{categoryId}/merch/{merchId} - удаление позиции мерча
    @DeleteMapping("/{merchId}")
    public ResponseEntity<Void> deleteMerch(@PathVariable UUID eventId,
                                            @PathVariable UUID categoryId,
                                            @PathVariable UUID merchId) {
        EventCategoryMerch existing = eventCategoryMerchRepository.findById(merchId)
                .orElseThrow(() -> new RuntimeException("Merch not found"));
        eventCategoryMerchRepository.delete(existing);
        return ResponseEntity.noContent().build();
    }
}
