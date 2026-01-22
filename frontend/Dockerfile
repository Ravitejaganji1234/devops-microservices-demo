# -------- Build Stage --------
FROM node:21-alpine AS builder

WORKDIR /app

# Copy dependency files first (for caching)
COPY package.json package-lock.json* ./

RUN npm ci

# Copy application source
COPY . .

# Build production assets
RUN npm run build

# -------- Runtime Stage --------
FROM nginx:alpine

COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80

ENTRYPOINT ["nginx", "-g", "daemon off;"]
