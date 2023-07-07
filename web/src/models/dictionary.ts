import {useEffect, useState} from "react";
import service from "@/services/dictionary";

interface DictEntryMap {
  [key: string]: SelectOption[];
}

export default () => {
  const [dictionary, setDictionary] = useState<DictEntryMap>({});

  useEffect(() => {
    service.getDictionary().then((res) => {
      setDictionary(res);
    })
  }, []);

  const getDictionary = (entryKey: string) => {
    entryKey = entryKey.toUpperCase();
    return dictionary[entryKey] || [];
  }

  const generateOptions = (entryKey: string) => {
    return getDictionary(entryKey);
  }

  return {
    dictionary,
    getDictionary,
    generateOptions
  }
}