import PageIllustration from "./page-illustration";

export default function HeroHome() {
  return (
    <section>
      <PageIllustration />
      <div className="mx-auto max-w-6xl px-4 sm:px-6">
        {/* Hero content */}
        <div className="pb-12 pt-32">
          {/* Section header */}
          <div className="text-center">
            <h1
              className="border-y text-5xl font-bold [border-image:linear-gradient(to_right,transparent,theme(colors.slate.300/.8),transparent)1] md:text-6xl"
            >
              <span className="text-blue-500">FAST</span>RAG <span className="text-purple-600">LLM</span>ENGINE
            </h1>
          </div>
        </div>
      </div>
    </section>
  );
}
