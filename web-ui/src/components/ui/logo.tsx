import { Link } from "react-router";
import { Image } from 'antd';

export default function Logo() {
  return (
    <Link to={{
        pathname: "/",
    }} className="inline-flex" aria-label="Cruip">
      <Image src="../../../public/logo.png" width={40} height={40} preview={false} />
    </Link>
  );
}
