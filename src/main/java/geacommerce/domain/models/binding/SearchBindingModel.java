package geacommerce.domain.models.binding;

import geacommerce.common.Constants;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SearchBindingModel {

    private String searchValue;

    public SearchBindingModel() {
    }

    @Pattern(regexp = "^[A-Za-z0-9\\s]+$", message = Constants.SEARCH_PATTERN_MESSAGE)
    public String getSearchValue() {
        return this.searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue.trim();
    }
}
