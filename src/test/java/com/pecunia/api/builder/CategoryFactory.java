package com.pecunia.api.builder;

import com.pecunia.api.model.Category;
import com.pecunia.api.model.CategoryType;
import com.pecunia.api.model.User;
import java.util.HashSet;
import java.util.Set;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

public class CategoryFactory {
  public static CategoryBuilder category() {
    return new CategoryBuilder();
  }

  public static class CategoryBuilder {
    private Category category = new Category();
    private User user;

    public CategoryBuilder withName(String name) {
      category.setCategoryName(name);
      return this;
    }

    public CategoryBuilder withIcon(String icon) {
      category.setIcon(icon);
      return this;
    }

    public CategoryBuilder withColor(String color) {
      category.setColor(color);
      return this;
    }

    public CategoryBuilder withType(CategoryType type) {
      category.setType(type);
      return this;
    }

    public CategoryBuilder isGlobal(boolean isGlobal) {
      category.setIsGlobal(isGlobal);
      return this;
    }

    public CategoryBuilder withUser(User user) {
      this.user = user;
      return this;
    }

    public Category build(TestEntityManager entityManager) {
      if (user == null) {
        throw new IllegalStateException(
            "Category muste have an user. Use 'withUser() static method.");
      }
      category.setUser(user);
      Set<Category> categories = user.getCategories();
      if (categories == null) {
        categories = new HashSet<>();
        user.setCategories(categories);
      }
      return entityManager.persistAndFlush(category);
    }
  }
}
