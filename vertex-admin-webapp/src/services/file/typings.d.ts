declare interface File {
  id: number;
  fileName: string;
  originalFilename: string;
  size: number;
  type: string;
  ext: string;
  contentType: string;
  userId: number;
  path: string;
  url: string;
  tempKey: string;
  expiredAt: string;
  attr: string;
  createdAt: string;
}

declare type FileType = 'image' | 'video' | 'audio' | 'document' | 'others';