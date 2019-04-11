package geacommerce.service;

import geacommerce.domain.models.service.InquiryServiceModel;

import java.util.List;

public interface InquiryService {

    boolean saveInquiry(InquiryServiceModel inquiryServiceModel);

    List<InquiryServiceModel> findAllInquiries();

    boolean readInquiry(String id);
}
