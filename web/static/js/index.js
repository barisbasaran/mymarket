$(document).ready(function () {
    for (let i = 0; i < 3; i++) {
        $("#mainTestimonial").append(getTestimonialRow());
    }
    testimonialSlider()
});

function getTestimonialRow() {
    return `<div className="item">
                <div className="row justify-content-center">
                    <div className="col-lg-8 mx-auto">
                
                        <div className="testimonial-block text-center">
                            <blockquote className="mb-5">
                                <p>&ldquo;Donec facilisis quam ut purus rutrum lobortis&rdquo;</p>
                            </blockquote>
                
                            <div className="author-info">
                                <div className="author-pic">
                                    <img src="/images/person-1.png" alt="Maria Jones" className="img-fluid"/>
                                </div>
                                <h3 className="font-weight-bold">Maria Jones</h3>
                                <span className="position d-block mb-3">CEO, Co-Founder, XYZ Inc.</span>
                            </div>
                        </div>
                
                    </div>
                </div>
             </div>`;
}
