package geacommerce.domain.models.binding;

import geacommerce.common.Constants;

import javax.validation.constraints.Pattern;

public class SearchBindingModel {

    private String searchValue;

    public SearchBindingModel() {
    }

    @Pattern(regexp = Constants.PRODUCT_SEARCH_PATTERN, message = Constants.SEARCH_PATTERN_MESSAGE)
    public String getSearchValue() {
        return this.searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue.trim();
    }
}
