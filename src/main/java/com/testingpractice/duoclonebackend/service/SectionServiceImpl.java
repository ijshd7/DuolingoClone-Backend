package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.BulkTree.SectionTreeNode;
import com.testingpractice.duoclonebackend.dto.BulkTree.UnitTreeNode;
import com.testingpractice.duoclonebackend.dto.SectionDto;
import com.testingpractice.duoclonebackend.entity.Lesson;
import com.testingpractice.duoclonebackend.entity.Section;
import com.testingpractice.duoclonebackend.entity.Unit;
import com.testingpractice.duoclonebackend.exception.ApiException;
import com.testingpractice.duoclonebackend.exception.ErrorCode;
import com.testingpractice.duoclonebackend.mapper.LessonMapper;
import com.testingpractice.duoclonebackend.mapper.SectionMapper;
import com.testingpractice.duoclonebackend.mapper.UnitMapper;
import com.testingpractice.duoclonebackend.repository.LessonRepository;
import com.testingpractice.duoclonebackend.repository.SectionRepository;
import com.testingpractice.duoclonebackend.repository.UnitRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SectionServiceImpl implements SectionService {

  private final SectionRepository sectionRepository;
  private final SectionMapper sectionMapper;
  private final UnitRepository unitRepository;
  private final UnitMapper unitMapper;
  private final LessonRepository lessonRepository;
  private final LessonMapper lessonMapper;
  private final LessonService lessonService;

  public SectionServiceImpl(
      SectionRepository sectionRepository,
      SectionMapper sectionMapper,
      UnitRepository unitRepository,
      UnitMapper unitMapper,
      LessonRepository lessonRepository,
      LessonMapper lessonMapper,
      LessonService lessonService) {
    this.sectionMapper = sectionMapper;
    this.sectionRepository = sectionRepository;
    this.unitRepository = unitRepository;
    this.unitMapper = unitMapper;
    this.lessonRepository = lessonRepository;
    this.lessonMapper = lessonMapper;
    this.lessonService = lessonService;
  }

  @Override
  public List<SectionDto> getSectionsByIds(List<Integer> sectionIds) {
    List<Section> sections = sectionRepository.findAllById(sectionIds);
    return sectionMapper.toDtoList(sections);
  }

  @Override
  public List<Integer> getSectionIdsByCourse(Integer courseId) {
    List<Integer> sectionIds = sectionRepository.findAllSectionIdsByCourseId(courseId);
    return sectionIds;
  }

  public SectionTreeNode getBulkSection(Integer sectionId, Integer userId) {
    Section section = sectionRepository.findById(sectionId).orElse(null);
    if (section == null) throw new ApiException(ErrorCode.SECTION_NOT_FOUND);

    SectionDto sectionDto = sectionMapper.toDto(section);

    List<Unit> units = unitRepository.findAllBySectionIdOrderByOrderIndexAsc(sectionId);
    List<Integer> unitIds = units.stream().map(Unit::getId).toList();

    List<Lesson> lessons = lessonRepository.findAllByUnitIdInOrderByUnitIdAscOrderIndexAsc(unitIds);

    Map<Integer, List<Lesson>> lessonsByUnit =
        lessons.stream()
            .collect(
                Collectors.groupingBy(Lesson::getUnitId, LinkedHashMap::new, Collectors.toList()));

    List<UnitTreeNode> unitNodes =
        units.stream()
            .map(
                u -> {
                  List<Lesson> ls = lessonsByUnit.getOrDefault(u.getId(), List.of());
                  return new UnitTreeNode(
                      unitMapper.toDto(u),
                      lessonMapper.toDtoList(
                          ls,
                          lessonService.completedSetFor(
                              userId, ls.stream().map(Lesson::getId).toList())));
                })
            .toList();

    return new SectionTreeNode(sectionDto, unitNodes);
  }
}
