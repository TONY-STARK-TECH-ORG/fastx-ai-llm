import { Image } from "antd";
import TestimonialImg from "../../public/logo.png";

export default function LargeTestimonial() {
  return (
    <section>
      <div className="mx-auto max-w-2xl px-4 sm:px-6">
        <div className="py-12 md:py-20">
          <div className="space-y-3 text-center">
            <div className="relative inline-flex">
              <Image
                className="rounded-full"
                src={TestimonialImg}
                width={48}
                height={48}
                alt="Large testimonial"
                preview={false}
              />
            </div>
            <p className="text-2xl font-bold text-gray-900">
              “Simple has simplified my life in more ways than one. From
              managing my sites to{" "}
              <em className="italic text-blue-500">keeping track of tasks</em>,
              it's become my go-to tool for everything.”
            </p>
            <div className="text-sm font-medium text-gray-500">
              <span className="text-gray-700">Mary Sullivan</span>{" "}
              <span className="text-gray-400">/</span>{" "}
              <a className="text-blue-500" href="/">
                CTO at Microsoft
              </a>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
