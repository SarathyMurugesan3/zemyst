package service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dto.BlogPostDTO;
import dto.ProductDTO;
import dto.SectionDTO;
import dto.TestimonialDTO;
import entity.BlogPost;
import entity.Product;
import entity.Section;
import entity.Testimonial;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import repository.BlogRepository;
import repository.ProductRepository;
import repository.SectionRepository;
import repository.TestimonialRepository;

@Service
public class LandingPageService {
    
    @Autowired
    private SectionRepository sectionRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private TestimonialRepository testimonialRepository;
    
    @Autowired
    private BlogRepository blogRepository;
    
    public List<SectionDTO> getAllSections() {
        return sectionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public byte[] getContactImage() {
        Optional<Section> contactSection = sectionRepository.findByName("contact");
        return contactSection.map(Section::getImage).orElse(null);
    }

    public SectionDTO getSectionByName(String name) {
        Optional<Section> section = sectionRepository.findByName(name);
        return section.map(this::convertToDTO).orElse(null);
    }
    
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<TestimonialDTO> getAllTestimonials() {
        return testimonialRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<BlogPostDTO> getAllBlogPosts() {
        return blogRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public byte[] getImage(String type, Long id) {
        byte[] image = null;
        switch (type) {
            case "section":
                image = sectionRepository.findById(id).map(Section::getImage).orElse(null);
                break;
            case "product":
                image = productRepository.findById(id).map(Product::getImage).orElse(null);
                break;
            case "impact":
            	image = sectionRepository.findById(id).map(Section::getImage).orElse(null);
            	break;
            case "testimonial":
                image = testimonialRepository.findById(id).map(Testimonial::getImage).orElse(null);
                break;
            case "blog":
                image = blogRepository.findById(id).map(BlogPost::getImage).orElse(null);
                break;
        }

        System.out.println("Fetched image for " + type + " ID " + id + " with length: " + (image != null ? image.length : 0));
        return image;
    }
    
    // Section operations
    public void updateSection(Long id, String content, MultipartFile image) throws IOException {
        Optional<Section> optionalSection = sectionRepository.findById(id);
        if (optionalSection.isPresent()) {
            Section section = optionalSection.get();
            section.setContent(content);
            if (image != null && !image.isEmpty()) {
                section.setImage(image.getBytes());
                section.setImageName(image.getOriginalFilename());
            }
            section.setUpdatedAt(LocalDateTime.now());
            sectionRepository.save(section);
        }
    }
    
    // Product operations
    public void updateProduct(Long id, String name, String description, MultipartFile image) throws IOException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(name);
            product.setDescription(description);
            if (image != null && !image.isEmpty()) {
                product.setImage(image.getBytes());  // Save image bytes
                product.setImageName(image.getOriginalFilename());
            }
            productRepository.save(product);
        }
    }

    public void createProduct(String name, String description, MultipartFile image) throws IOException {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        if (image != null && !image.isEmpty()) {
            product.setImage(image.getBytes());  // Save image bytes
            product.setImageName(image.getOriginalFilename());
        }
        productRepository.save(product);
    }

    
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    // Testimonial operations
    public void createTestimonial(int rating,String name, String message, String position, MultipartFile image) throws IOException {
        Testimonial testimonial = new Testimonial();
        testimonial.setRating(rating);
        testimonial.setName(name);
        testimonial.setMessage(message);
        testimonial.setPosition(position);
        if (image != null && !image.isEmpty()) {
            testimonial.setImage(image.getBytes());
            testimonial.setImageName(image.getOriginalFilename());
        }
        testimonialRepository.save(testimonial);
    }
    
    public void deleteTestimonial(Long id) {
        testimonialRepository.deleteById(id);
    }
    
    // Blog operations
    public void createBlogPost(String title, String content, MultipartFile image) throws IOException {
        BlogPost blogPost = new BlogPost();
        blogPost.setTitle(title);
        blogPost.setContent(content);
        if (image != null && !image.isEmpty()) {
            blogPost.setImage(image.getBytes());
            blogPost.setImageName(image.getOriginalFilename());
        }
        blogRepository.save(blogPost);
    }
    
    public void deleteBlogPost(Long id) {
        blogRepository.deleteById(id);
    }
 // Edit Testimonial
    public void updateTestimonial(Long id, int rating,String name, String message, String position, MultipartFile image) throws IOException {
        Optional<Testimonial> optionalTestimonial = testimonialRepository.findById(id);
        if (optionalTestimonial.isPresent()) {
            Testimonial testimonial = optionalTestimonial.get();
            testimonial.setName(name);
            testimonial.setMessage(message);
            testimonial.setPosition(position);
            testimonial.setRating(rating);
            if (image != null && !image.isEmpty()) {
                testimonial.setImage(image.getBytes());
                testimonial.setImageName(image.getOriginalFilename());
            }
            testimonialRepository.save(testimonial);
        }
    }

    // Edit BlogPost
    public void updateBlogPost(Long id, String title, String content, MultipartFile image) throws IOException {
        Optional<BlogPost> optionalBlogPost = blogRepository.findById(id);
        if (optionalBlogPost.isPresent()) {
            BlogPost blogPost = optionalBlogPost.get();
            blogPost.setTitle(title);
            blogPost.setContent(content);
            if (image != null && !image.isEmpty()) {
                blogPost.setImage(image.getBytes());
                blogPost.setImageName(image.getOriginalFilename());
            }
            blogRepository.save(blogPost);
        }
    }

    // DTO conversion methods
    private SectionDTO convertToDTO(Section section) {
        SectionDTO dto = new SectionDTO();
        dto.setId(section.getId());
        dto.setName(section.getName());
        dto.setContent(section.getContent());
        dto.setImageName(section.getImageName());
        dto.setHasImage(section.getImage() != null);
        if (section.getName().equalsIgnoreCase("contact")) {
            SectionDTO.ContactInfo contactInfo = new SectionDTO.ContactInfo();
            contactInfo.setEmail("info@example.com");
            contactInfo.setPhone("+91 9876543210");
            dto.setContactInfo(contactInfo);
        }


        return dto;
    }
    
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setImageName(product.getImageName());
        dto.setHasImage(product.getImage() != null && product.getImage().length > 0);  // Check length > 0
        return dto;
    }

    
    private TestimonialDTO convertToDTO(Testimonial testimonial) {
        TestimonialDTO dto = new TestimonialDTO();
        dto.setId(testimonial.getId());
        dto.setRating(testimonial.getRating());
        dto.setName(testimonial.getName());
        dto.setMessage(testimonial.getMessage());
        dto.setPosition(testimonial.getPosition());
        dto.setImageName(testimonial.getImageName());
        dto.setHasImage(testimonial.getImage() != null);
        return dto;
    }
    public void sendContactMail(String to, String subject, String body) {
        SectionDTO contactSection = getSectionByName("contact");

        if (contactSection == null || contactSection.getContactInfo() == null) {
            throw new RuntimeException("Contact section or email info missing");
        }

        String senderEmail = contactSection.getContactInfo().getEmail();
        String appPassword = contactSection.getContent(); // app password stored in content field

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(senderEmail);
        mailSender.setPassword(appPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");

            helper.setTo(to);
            helper.setFrom(senderEmail);
            helper.setSubject(subject);
            helper.setText(body, false);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Email sending failed: " + e.getMessage());
        }
    }
    public void updateSection(SectionDTO dto) {
        Optional<Section> optionalSection = sectionRepository.findById(dto.getId());
        if (optionalSection.isPresent()) {
            Section section = optionalSection.get();
            section.setContent(dto.getContent());
            section.setUpdatedAt(LocalDateTime.now());
            
            // Optional: update contact info if this is the contact section
            if ("contact".equalsIgnoreCase(section.getName()) && dto.getContactInfo() != null) {
                // Store contact info in some way if needed
                // You can update section.setContent(), setEmail(), etc.
            }

            sectionRepository.save(section);
        } else {
            throw new RuntimeException("Section with ID " + dto.getId() + " not found");
        }
    }

    private BlogPostDTO convertToDTO(BlogPost blogPost) {
        BlogPostDTO dto = new BlogPostDTO();
        dto.setId(blogPost.getId());
        dto.setTitle(blogPost.getTitle());
        dto.setContent(blogPost.getContent());
        dto.setImageName(blogPost.getImageName());
        dto.setHasImage(blogPost.getImage() != null);
        return dto;
    }
}
