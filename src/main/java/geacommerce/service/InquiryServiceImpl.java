package geacommerce.service;

import geacommerce.domain.entities.Inquiry;
import geacommerce.domain.models.service.InquiryServiceModel;
import geacommerce.repository.InquiryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<InquiryServiceModel> findAllInquiries() {
        return this.inquiryRepository.findAll()
                .stream()
                .map(inquiry -> this.modelMapper.map(inquiry, InquiryServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void readInquiry(String id) {
        try {
            this.inquiryRepository.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
