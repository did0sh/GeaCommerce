package geacommerce.service;

import geacommerce.domain.entities.Inquiry;
import geacommerce.domain.models.service.InquiryServiceModel;
import geacommerce.repository.InquiryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class InquiryServiceTests {

    @Autowired
    InquiryService inquiryService;

    @MockBean
    InquiryRepository mockInquiryRepository;

    private InquiryServiceModel inquiryServiceModel;

    private Inquiry inquiry;

    private List<Inquiry> inquiryList;

    @Before
    public void setup(){
        this.inquiryServiceModel = new InquiryServiceModel(){{
            setId("test id");
            setMessage("Test message");
            setUserEmail("test@email.com");
            setUserName("Test username");
        }};

        this.inquiry = new Inquiry(){{
            setId(inquiryServiceModel.getId());
            setMessage(inquiryServiceModel.getMessage());
            setUserEmail(inquiryServiceModel.getUserEmail());
            setUserName(inquiryServiceModel.getUserName());
        }};

        this.inquiryList = new ArrayList<>(){{
            add(inquiry); add(inquiry);
        }};

        when(mockInquiryRepository.findAll())
                .thenReturn(this.inquiryList);
    }

    @Test
    public void saveInquiry_whenInquiryNull_ShouldReturnFalse(){
        boolean isSaved = this.inquiryService.saveInquiry(null);
        assertFalse(isSaved);
    }

    @Test
    public void saveInquiry_whenInquiryValid_ShouldReturnTrue(){
        boolean isSaved = this.inquiryService.saveInquiry(inquiryServiceModel);
        assertTrue(isSaved);
    }

    @Test
    public void findAllInquiries_whenListEmpty_ShouldReturnEmptyList(){
        this.inquiryList.clear();
        List<InquiryServiceModel> inquiries = this.inquiryService.findAllInquiries();
        assertTrue(inquiries.isEmpty());
        assertEquals(0, inquiries.size());
    }

    @Test
    public void findAllInquiries_whenListHasInquiries_ShouldReturnCorrectSize(){
        List<InquiryServiceModel> inquiries = this.inquiryService.findAllInquiries();
        assertEquals(2, inquiries.size());
    }

    @Test
    public void readInquiry_whenIdIsCorrect_ShouldReturnTrue(){
        boolean isRead = this.inquiryService.readInquiry(inquiryServiceModel.getId());
        assertTrue(isRead);
    }

}
