package net.dudko.project.service.impl;

import lombok.AllArgsConstructor;
import net.dudko.project.domain.repository.CategoryRepository;
import net.dudko.project.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    //TODO: Create implements

}
