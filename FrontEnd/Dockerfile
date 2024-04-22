# Sử dụng một base image chứa Node.js để xây dựng ứng dụng
FROM node:14 as build

# Đặt thư mục làm việc là /app
WORKDIR /app

# Sao chép tệp package.json và package-lock.json (hoặc yarn.lock) vào thư mục hiện tại
COPY package*.json ./

# Cài đặt các phụ thuộc của ứng dụng
RUN npm install

# Sao chép mã nguồn của ứng dụng vào thư mục hiện tại
COPY . .

# Xây dựng ứng dụng Vue.js
RUN npm run build

# Sử dụng một web server đơn giản để phục vụ các tệp tĩnh
FROM nginx:alpine

# Sao chép các tệp đã được xây dựng từ stage trước vào thư mục mặc định của nginx để phục vụ ứng dụng
COPY --from=build /app/dist /usr/share/nginx/html

# Port mặc định cho nginx
EXPOSE 80

# Khởi động nginx khi container được khởi chạy
CMD ["nginx", "-g", "daemon off;"]
