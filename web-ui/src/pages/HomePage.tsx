import Header from "../components/ui/header.tsx";
import Footer from "../components/ui/footer.tsx";
import Hero from "../components/hero-home.tsx";
import BusinessCategories from "../components/business-categories.tsx";
import FeaturesPlanet from "../components/features-planet.tsx";
import LargeTestimonial from "../components/large-testimonial";
import Cta from "../components/cta";

import { useEffect } from "react";
import AOS from "aos";
import "aos/dist/aos.css";

export default function HomePage() {
    useEffect(() => {
        AOS.init({
            once: true,
            disable: "phone",
            duration: 700,
            easing: "ease-out-cubic",
        });
    }, []);
    return (
        <>
            <Header />
            <main className="grow">
                <Hero />
                <BusinessCategories />
                <FeaturesPlanet />
                <LargeTestimonial />
                <Cta />
            </main>
            <Footer border={true} />
        </>
    );
}