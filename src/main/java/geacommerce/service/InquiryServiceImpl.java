package geacommerce.service;

import geacommerce.domain.entities.Inquiry;
import geacommerce.domain.models.service.InquiryServiceModel;
import geacommerce.repository.InquiryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InquiryServiceImpl implements InquiryService {
    private final ModelMapper modelMapper;
    private final InquiryRepository inquiryRepository;

    @Autowired
    public InquiryServiceImpl(ModelMapper modelMapper, InquiryRepository inquiryRepository) {
        this.modelMapper = modelMapper;
        this.inquiryRepository = inquiryRepository;
    }

    @Override
    public boolean saveInquiry(InquiryServiceModel inquiryServiceModel) {
        try {
            Inquiry inquiry = this.modelMapper.map(inquiryServiceModel, Inquiry.class);
            this.inquiryRepository.saveAndFlush(inquiry);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
