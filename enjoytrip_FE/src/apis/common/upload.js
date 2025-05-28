import apiAxios from "@/apis/axios.js";
import axios from "axios";

export const getPresignedUrl = (imageName) => {
  return apiAxios.get(`/upload/presign-url`, {
    params: {
      filename: `${encodeURIComponent(imageName)}`,
    },
  });
};

export const uploadImage = async (image) => {
  const { url } = await getPresignedUrl(image.name);

  new axios.put(url, image, {
    headers: {
      "Content-type": image.type,
    },
  });
  const imgUrl = new URL(url);
  const path = imgUrl.origin + imgUrl.pathname;
  console.log(path);
  return path;
};
