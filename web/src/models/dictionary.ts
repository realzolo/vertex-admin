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

  const getOptions = (entryKey: string) => {
    entryKey = entryKey
      .replace(/([A-Z])/g, "_$1")
      .replace(/^_/, "")
      .toUpperCase();
    return getDictionary(entryKey).map((item) => {
      return {
        label: item.label,
        value: item.label
      }
    });
  }

  return {
    dictionary,
    getDictionary,
    getOptions
  }
}