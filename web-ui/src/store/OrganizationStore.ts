import { createWithEqualityFn } from 'zustand/traditional'
import {createJSONStorage, devtools} from 'zustand/middleware'
import {persist} from 'zustand/middleware'
import {OrganizationAction} from "./organization/Organization.ts";
import type {StateCreator} from "zustand/vanilla";
import {Organization} from "./define.ts";

// combine type and actions
export type OrganizationStore = Organization & OrganizationAction;

// create store
const userStore: StateCreator<OrganizationStore> = (set) => ({
    id: undefined,
    createTime: "",
    updateTime: "",
    name: "",
    // user actions
    selectOrganization: (organization: Organization | undefined) => {
        if (!organization) {
            set({
                id: undefined,
                createTime: "",
                updateTime: "",
                name: undefined
            })
            return ;
        }
        set({ ...organization })
    },
});

const persistentOptions = {
    name: "organization-select-storage",
    storage: createJSONStorage(() => localStorage)
}
// export user store
export const useOrganizationStore = createWithEqualityFn<OrganizationStore>()(
    devtools(persist(userStore, persistentOptions))
)